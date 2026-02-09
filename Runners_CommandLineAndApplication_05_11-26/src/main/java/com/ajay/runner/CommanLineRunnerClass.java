package com.ajay.runner;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("runner")
@Order()
public class CommanLineRunnerClass implements CommandLineRunner,Ordered{

	@Override
	public void run(String... args) throws Exception {
		System.out.println("order 1 "+Arrays.toString(args).toString());
	}

	@Override
	public int getOrder() {
		return 0;
	}

	

}

