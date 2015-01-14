package bgmap.core.entity;

/**
 * 1. полное наименование субъекта ведения хозяйства
 * 2. адрес субъекта ведения хозяйства
 * 3. номер государственной регистрации субъекта ведения хозяйства
 * 4. телефон
 * 5. электронный адрес
 * 6. назначение малой архитектурной формы
 * 7. адрес или приблизительное желаемое место размещения
 * 8. Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, место расположения, площадь, кв. метров)
 * 9. Дата и номер паспорта привязки
 * 10. Фамилия, имя и отчество уполномоченного лица
 */
public class Maf {
	
	private int x;
	private int y;
	private byte colNum;
	private byte rowNum;
	private String subjectName;
	private String subjectAddress;
	private int subjectRegNum;
	private String telephone;
	private String site;
	private String purpose;
	private String objectAddress;
	private String techCharacteristics;
	private String passport;
	private String personFullName;
	
	public Maf(int x, int y, byte colNum, byte rowNum, String subjectName, String subjectAddress, int subjectRegNum,
			String telephone, String site, String purpose,
			String objectAddress, String techCharacteristics, String passport,
			String personFullName) {
		this.x = x;
		this.y = y;
		this.colNum = colNum;
		this.rowNum = rowNum;
		this.subjectName = subjectName;
		this.subjectAddress = subjectAddress;
		this.subjectRegNum = subjectRegNum;
		this.telephone = telephone;
		this.site = site;
		this.purpose = purpose;
		this.objectAddress = objectAddress;
		this.techCharacteristics = techCharacteristics;
		this.passport = passport;
		this.personFullName = personFullName;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectAddress() {
		return subjectAddress;
	}
	public void setSubjectAddress(String subjectAddress) {
		this.subjectAddress = subjectAddress;
	}
	public int getSubjectRegNum() {
		return subjectRegNum;
	}
	public void setSubjectRegNum(int subjectRegNum) {
		this.subjectRegNum = subjectRegNum;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getObjectAddress() {
		return objectAddress;
	}
	public void setObjectAddress(String objectAddress) {
		this.objectAddress = objectAddress;
	}
	public String getTechCharacteristics() {
		return techCharacteristics;
	}
	public void setTechCharacteristics(String techCharacteristics) {
		this.techCharacteristics = techCharacteristics;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	public String getPersonFullName() {
		return personFullName;
	}
	public void setPersonFullName(String personFullName) {
		this.personFullName = personFullName;
	}

	public byte getColNum() {
		return colNum;
	}

	public void setColNum(byte colNum) {
		this.colNum = colNum;
	}

	public byte getRowNum() {
		return rowNum;
	}

	public void setRowNum(byte rowNum) {
		this.rowNum = rowNum;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
