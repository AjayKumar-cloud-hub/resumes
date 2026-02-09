package com.ajay.runner;


import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("appRunner")
@Order(-1)
public class ApplicationRunnerClass implements ApplicationRunner{

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("app runner"+new Date());
		System.out.println("Non-Option args "+args.getNonOptionArgs());
		List<String> args2 = args.getNonOptionArgs();
		System.out.println(args2);
		Set<String> names = args.getOptionNames();
		names.forEach(name -> {
			System.out.println(args.getOptionValues(name));
		});
	}

}
