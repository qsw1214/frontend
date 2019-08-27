package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.util.ApprovalInfUtil;
import cc.mrbird.febs.dingding.util.MessageUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;

import java.util.Map;

/**
 * 审批控制层
 * @Slf4j 方便写日志 log.info()、log.error(),集中起来处理
 * @Validated 异常时不直接返回错误,而是对数据进行校验
 * @RestController
 */
@Slf4j
@Validated
@RestController
@RequestMapping("approval")
public class ApprovalController {
    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private static final Logger mainLogger = LoggerFactory.getLogger(ApprovalController.class);

    /**
     * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String CHECK_URL = "check_url";

    /**
     * 审批任务回调
     */
    private static final String BPMS_TASK_CHANGE = "bpms_task_change";

    /**
     * 审批实例回调
     */
    private static final String BPMS_INSTANCE_CHANGE = "bpms_instance_change";

    /**
     * 相应钉钉回调时的值
     */
    private static final String CALLBACK_RESPONSE_SUCCESS = "success";

    @RequestMapping(value = "/callback")
    @ResponseBody
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) JSONObject json) {
        String params = " signature:"+signature + " timestamp:"+timestamp +" nonce:"+nonce+" json:"+json;
        try {
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY,
                    Constant.CORP_ID);
            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            JSONObject obj = JSON.parseObject(plainText);

            //根据回调数据类型做不同的业务处理
            String eventType = obj.getString("EventType");
            //审批任务开始，结束，转交

            if (BPMS_TASK_CHANGE.equals(eventType)) {
                bizLogger.info("收到审批任务进度更新: " + plainText);

                //todo: 实现审批的业务逻辑，如发消息
                //审批实例开始，结束
                //调用审批审批详情接口
                Map map = ApprovalInfUtil.getToken(obj.getString("processInstanceId"));
                System.out.println(map);
                //通过这个map获取到的信息,同步数据库的数据
                //CallBackService.insertOrUpdateApprovalInf(map);
            } else if (BPMS_INSTANCE_CHANGE.equals(eventType)) {
                bizLogger.info("收到审批实例状态更新: " + plainText);
                //调用审批审批详情接口
                Map map = ApprovalInfUtil.getToken(obj.getString("processInstanceId"));
                //通过这个map获取到的信息,同步数据库的数据
                //CallBackService.insertOrUpdateApprovalInf(map);
                //todo: 实现审批的业务逻辑，如发消息
                String processInstanceId = obj.getString("processInstanceId");
                if (obj.containsKey("result") && obj.getString("result").equals("agree")) {
                    MessageUtil.sendMessageToOriginator(processInstanceId);
                }
            } else {
                // 其他类型事件处理
            }

            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }

    }
}
