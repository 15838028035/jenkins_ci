package com.thinkit.cloud.jenkinsci.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.thinkit.cloud.jenkinsci.bean.BuildInfoVo;

@Component
@ConfigurationProperties("builds") //接收application.yml中的myProps下面的属性
public class MyProps {
	private List<BuildInfoVo> buildjob = new ArrayList<BuildInfoVo>(); //接收prop2里面的属性值

	public List<BuildInfoVo> getBuildjob() {
		return buildjob;
	}

	public void setBuildjob(List<BuildInfoVo> buildjob) {
		this.buildjob = buildjob;
	}


}
