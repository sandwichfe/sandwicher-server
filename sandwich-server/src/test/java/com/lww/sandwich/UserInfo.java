package com.lww.sandwich;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserInfo {
    @ExcelProperty("姓名")
    private String name;    // 姓名

    @ExcelProperty("First Name")
    private String firstName;    // 姓名

    @ExcelProperty("电子邮件")
    private String email;        // 电子邮件

    @ExcelProperty("E-mail Address")
    private String emailAddress;        // 电子邮件

    @ExcelProperty("手机")
    private String mobilePhone;  // 手机

    @ExcelProperty("Home Phone")
    private String homePhone;  // 手机

    @ExcelProperty("别名")
    private String alias;
    // 别名
    @ExcelProperty("生日")
    private String birthDate;    // 生日

    @ExcelProperty("备用邮箱")
    private String backupEmail;  // 备用邮箱

    @ExcelProperty("家庭地址")
    private String homeAddress;  // 家庭地址

    @ExcelProperty("公司名称")
    private String companyName;  // 公司名称

    @ExcelProperty("备注")
    private String remarks;      // 备注

    public UserInfo(String name, String firstName, String email, String emailAddress, String mobilePhone, String homePhone, String alias, String birthDate, String backupEmail, String homeAddress, String companyName, String remarks) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.emailAddress = emailAddress;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.alias = alias;
        this.birthDate = birthDate;
        this.backupEmail = backupEmail;
        this.homeAddress = homeAddress;
        this.companyName = companyName;
        this.remarks = remarks;
    }

    // Getter 和 Setter
    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getAlias() {
        return alias;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBackupEmail() {
        return backupEmail;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setBackupEmail(String backupEmail) {
        this.backupEmail = backupEmail;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
