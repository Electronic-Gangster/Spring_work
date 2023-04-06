package study1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Ex1Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 자바 방식을 이용한 호출
		MessageInter m1 = new Message1();
		m1.sayHello("박근혜");
		
		MessageInter m2 = new Message2();
		m2.sayHello("이명박");
		
		//스프링 방식으로 호출
		ApplicationContext context=new ClassPathXmlApplicationContext("appContext.xml");
		//방법 1
		MessageInter m3=(Message1)context.getBean("mes1");
		
		//방법 2 scope를 이용한 주소 표
		MessageInter m33=(Message1)context.getBean("mes1");
		
		//방법 2
		MessageInter m4=context.getBean("mes2", Message2.class);
		
		//출력
		m3.sayHello("윤석열");
		m33.sayHello("김건희");
		// scope="singleton" 인 경우 두 주소가 서로 같다. (기본값이 singleton)
		// scope="prototype" 인 경우는 두 주소가 서로 다르다.
		System.out.println("주소 비교 : "+m3.hashCode()+":"+m33.hashCode());
		m4.sayHello("문재인");
	}

}
