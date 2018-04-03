

/**
 * Employee class which holds first name, last name, phone number, and department
 * @author Andrew Huelsman
 *
 */
public class Employee {

	private String fname, lname, phone, dept, title, gender;

	public String get_lname() {
		return lname;
	}
	public String get_fname() {
		return fname;
	}
	public String getTitle()
	{
		return title;
	}
	public String getPhone()
	{
		return phone;
	}
	public String getGender()
	{
		return gender;
	}
	public String getDept()
	{
		return dept;
	}

	public Employee(String fname, String lname, String dept, String phone, String gender, String title)
	{
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.dept = dept;
		this.gender=gender;
		this.title=title;
	}

	public void set_fname(String fname) {
		this.fname = fname;
	}

	public void set_lname(String lname) {
		this.lname = lname;
	}

	public void set_phone(String phone) {
		this.phone = phone;
	}

	public void set_department(String department) {
		this.dept = department;
	}
	
	public void set_gender(String gender) {
		this.gender = gender;
	}
	
	
	@Override
	public String toString()
	{
		return title + " " + lname + ", " + fname + " (" + gender + ") in " + dept + " Contact: "+ phone;
	}
}
