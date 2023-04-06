package study2;

public class Student {
	String schoolName;
	
	MyInfo myInfo;
	
	public Student(MyInfo myInfo) {
		this.myInfo=myInfo;
	}
	
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	public void show()
	{
		System.out.println(myInfo);
		System.out.println("학교명: "+schoolName);
	}
}
