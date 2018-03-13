

/**
 * Employee class which holds first name, last name, phone number, and department
 * @author Andrew Huelsman
 *
 */
public class Employee {

	private String _fname, _lname, _phone, _department, _title, _gender;

	public String get_lname() {
		return _lname;
	}
	public String get_fname() {
		return _fname;
	}

	public Employee(String fname, String lname, String dept, String phone, String gender, String title)
	{
		_fname = fname;
		_lname = lname;
		_phone = phone;
		_department = dept;
		_gender=gender;
		_title=title;
	}

	public void set_fname(String fname) {
		_fname = fname;
	}

	public void set_lname(String lname) {
		_lname = lname;
	}

	public void set_phone(String phone) {
		_phone = phone;
	}

	public void set_department(String department) {
		_department = department;
	}
	
	public void set_gender(String gender) {
		_gender = gender;
	}
	
	
	@Override
	public String toString()
	{
		return _title + " " + _lname + ", " + _fname + " (" + _gender + ") in " + _department + " Contact: "+ _phone;
	}
}
