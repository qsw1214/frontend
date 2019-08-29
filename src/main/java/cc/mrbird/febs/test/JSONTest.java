package cc.mrbird.febs.test;

import cc.mrbird.febs.dingding.vo.DingFormVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JSONTest {

    public static void main(String args[]){
        Gson gson = new Gson();
        String formComponentValues = "[{\"component_type\":\"TextField\",\"name\":\"学校名称\",\"id\":\"TextField-JZRUIUC2\",\"value\":\"湖南长郡中学\"},\n" +
                "{\"component_type\":\"TextField\",\"name\":\"负责人\",\"id\":\"TextField-JZRUIUC3\",\"value\":\"陈瑶\"},\n" +
                "{\"component_type\":\"DDSelectField\",\"name\":\"学校类别\",\"ext_value\":\"{\\\"key\\\":\\\"option_0\\\"}\",\"id\":\"DDSelectField-JZRUIUC4\",\"value\":\"主校\"}\n" +
                "]";
        Type type = new TypeToken<List<DingFormVO>>() {}.getType();
        List<DingFormVO> dingFormVo = gson.fromJson(formComponentValues, type);
        System.out.println(dingFormVo.size());
    }
}
