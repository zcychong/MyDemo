package com.healthmanage.ylis.model;

/**
 * 用户VO.
 * 
 * @author liuym
 * @Table User
 * @date 2016-10-28
 */
public class LoginResponse extends BaseResponseEntity {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 真实姓名
	 */
	private String name;

	/**
	 * 用户号
	 */
	private String userNo;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 证件号码
	 */
	private String cardNo;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 移动电话
	 */
	private String mobilephone;

	/**
	 * EMAIL
	 */
	private String email;

	/**
	 * 用户类型
	 */
	private String type;

	/**
	 * 用户状态
	 */
	private String status;

	/**
	 * 到期日期
	 */
	private String dueTime;

	/**
	 * 录入日期
	 */
	private String inputTime;

	/**
	 * 录入人员
	 */
	private String inputUser;

	/**
	 * 所属机构
	 */
	private String usrOrg;

	/**
	 * 岗位状态
	 */
	private String postSta;

	/**
	 * 服务评级
	 */
	private String fwpj;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 参加工作时间
	 */
	private String joinJobDate;

	/**
	 * 学历
	 */
	private String ecucBack;

	/**
	 * 护理职称
	 */
	private String hlzc;

	/**
	 * 职业证号
	 */
	private String zyzh;

	/**
	 * 服装鞋码
	 */
	private String fzxm;

	/**
	 * 标示用户是否交接任务
	 */
	private String shiftId;
	
	/**
	 * 班次
	 */
	private String execTime;
	
	/**
	 * 是否有交接遗留任务
	 */
	private boolean completStatus;
	
	public boolean isCompletStatus() {
		return completStatus;
	}

	public void setCompletStatus(boolean completStatus) {
		this.completStatus = completStatus;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	/**
	 * @方法描述：读取用户ID
	 * @return 用户ID
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @方法描述：用户ID
	 * @param String
	 *            userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @方法描述：读取真实姓名
	 * @return 真实姓名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @方法描述：真实姓名
	 * @param String
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @方法描述：读取用户号
	 * @return 用户号
	 */
	public String getUserNo() {
		return this.userNo;
	}

	/**
	 * @方法描述：用户号
	 * @param String
	 *            userNo
	 */
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	/**
	 * @方法描述：读取密码
	 * @return 密码
	 */
	public String getPwd() {
		return this.pwd;
	}

	/**
	 * @方法描述：密码
	 * @param String
	 *            pwd
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @方法描述：读取证件号码
	 * @return 证件号码
	 */
	public String getCardNo() {
		return this.cardNo;
	}

	/**
	 * @方法描述：证件号码
	 * @param String
	 *            cardNo
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * @方法描述：读取性别
	 * @return 性别
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * @方法描述：性别
	 * @param String
	 *            gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @方法描述：读取移动电话
	 * @return 移动电话
	 */
	public String getMobilephone() {
		return this.mobilephone;
	}

	/**
	 * @方法描述：移动电话
	 * @param String
	 *            mobilephone
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	/**
	 * @方法描述：读取EMAIL
	 * @return EMAIL
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @方法描述：EMAIL
	 * @param String
	 *            email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @方法描述：读取用户类型
	 * @return 用户类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @方法描述：用户类型
	 * @param String
	 *            type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @方法描述：读取用户状态
	 * @return 用户状态
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @方法描述：用户状态
	 * @param String
	 *            status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @方法描述：读取到期日期
	 * @return 到期日期
	 */
	public String getDueTime() {
		return this.dueTime;
	}

	/**
	 * @方法描述：到期日期
	 * @param String
	 *            dueTime
	 */
	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}

	/**
	 * @方法描述：读取录入日期
	 * @return 录入日期
	 */
	public String getInputTime() {
		return this.inputTime;
	}

	/**
	 * @方法描述：录入日期
	 * @param String
	 *            inputTime
	 */
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	/**
	 * @方法描述：读取录入人员
	 * @return 录入人员
	 */
	public String getInputUser() {
		return this.inputUser;
	}

	/**
	 * @方法描述：录入人员
	 * @param String
	 *            inputUser
	 */
	public void setInputUser(String inputUser) {
		this.inputUser = inputUser;
	}

	/**
	 * @方法描述：读取所属机构
	 * @return 所属机构
	 */
	public String getUsrOrg() {
		return this.usrOrg;
	}

	/**
	 * @方法描述：所属机构
	 * @param String
	 *            usrOrg
	 */
	public void setUsrOrg(String usrOrg) {
		this.usrOrg = usrOrg;
	}

	/**
	 * @方法描述：读取岗位状态
	 * @return 岗位状态
	 */
	public String getPostSta() {
		return this.postSta;
	}

	/**
	 * @方法描述：岗位状态
	 * @param String
	 *            postSta
	 */
	public void setPostSta(String postSta) {
		this.postSta = postSta;
	}

	/**
	 * @方法描述：读取服务评级
	 * @return 服务评级
	 */
	public String getFwpj() {
		return this.fwpj;
	}

	/**
	 * @方法描述：服务评级
	 * @param String
	 *            fwpj
	 */
	public void setFwpj(String fwpj) {
		this.fwpj = fwpj;
	}

	/**
	 * @方法描述：读取出生日期
	 * @return 出生日期
	 */
	public String getBirthday() {
		return this.birthday;
	}

	/**
	 * @方法描述：出生日期
	 * @param String
	 *            birthday
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @方法描述：读取参加工作时间
	 * @return 参加工作时间
	 */
	public String getJoinJobDate() {
		return this.joinJobDate;
	}

	/**
	 * @方法描述：参加工作时间
	 * @param String
	 *            joinJobDate
	 */
	public void setJoinJobDate(String joinJobDate) {
		this.joinJobDate = joinJobDate;
	}

	/**
	 * @方法描述：读取学历
	 * @return 学历
	 */
	public String getEcucBack() {
		return this.ecucBack;
	}

	/**
	 * @方法描述：学历
	 * @param String
	 *            ecucBack
	 */
	public void setEcucBack(String ecucBack) {
		this.ecucBack = ecucBack;
	}

	/**
	 * @方法描述：读取护理职称
	 * @return 护理职称
	 */
	public String getHlzc() {
		return this.hlzc;
	}

	/**
	 * @方法描述：护理职称
	 * @param String
	 *            hlzc
	 */
	public void setHlzc(String hlzc) {
		this.hlzc = hlzc;
	}

	/**
	 * @方法描述：读取职业证号
	 * @return 职业证号
	 */
	public String getZyzh() {
		return this.zyzh;
	}

	/**
	 * @方法描述：职业证号
	 * @param String
	 *            zyzh
	 */
	public void setZyzh(String zyzh) {
		this.zyzh = zyzh;
	}

	/**
	 * @方法描述：读取服装鞋码
	 * @return 服装鞋码
	 */
	public String getFzxm() {
		return this.fzxm;
	}

	/**
	 * @方法描述：服装鞋码
	 * @param String
	 *            fzxm
	 */
	public void setFzxm(String fzxm) {
		this.fzxm = fzxm;
	}
}
