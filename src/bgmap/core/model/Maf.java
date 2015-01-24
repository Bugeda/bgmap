package bgmap.core.model;

import bgmap.core.MafsMarks;

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
	
	final public static String SubjectNameField = "Полное наименование субъекта ведения хозяйства";
	final public static String SubjectAddressField = "Адрес субъекта ведения хозяйства";
	final public static String SubjectRegNumField = "Номер государственной регистрации субъекта ведения хозяйства";
	final public static String TelephoneField = "Телефон";
	final public static String SiteField = "Электронный адрес";
	final public static String PurposeField = "Назначение малой архитектурной формы";
	final public static String ObjectAddressField = "Адрес или приблизительное желаемое место размещения";
	final public static String TechCharacteristicsField = "Технические характеристики малой архитектурной формы ( стационарная или передвижная, павильон, киоск, временное устройство для сезонной торговли, размеры, площадь, кв. метров)";
	final public static String PassportField = "Дата и номер паспорта привязки";
	final public static String PersonFullNameField = "Фамилия, имя и отчество уполномоченного лица";
	
	private short x;
	private short y;
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
	private MafsMarks mafMark;
	
	public Maf(short x, short y, byte colNum, byte rowNum, String subjectName, String subjectAddress, int subjectRegNum,
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
		
		if ((subjectName.length() == 0)||
			(subjectAddress.length() == 0)||	
			(subjectRegNum == 0)||
			(telephone.length() == 0)||
			(site.length() == 0)||
			(purpose.length() == 0)||
			(objectAddress.length() == 0)||
			(techCharacteristics.length() == 0)||
			(passport.length() == 0)||
			(personFullName.length() == 0))
			this.mafMark = MafsMarks.SIGN;
		else this.mafMark = MafsMarks.SIGNFULL;
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

	public void setX(short x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public MafsMarks getMafMark() {
		return mafMark;
	}	
}
