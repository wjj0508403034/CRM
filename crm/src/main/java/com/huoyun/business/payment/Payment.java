package com.huoyun.business.payment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.joda.time.DateTime;

import com.huoyun.business.contract.Contract;
import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class Payment extends AbstractBusinessObjectImpl {

	public Payment() {
	}

	public Payment(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;
	
	@BoProperty(readonly= true)
	private String paymentNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@BoProperty(readonly= true)
	private Contract contract;

	@BoProperty(type = PropertyType.Date)
	private DateTime paymentDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@BoProperty
	private Employee payee;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private PaymentTerm paymentTerm;

	@BoProperty(type = PropertyType.Price)
	private double amount;

	@BoProperty(type = PropertyType.Text)
	private String memo;

	@Override
	public Long getId() {
		return this.id;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public DateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(DateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Employee getPayee() {
		return payee;
	}

	public void setPayee(Employee payee) {
		this.payee = payee;
	}

	public PaymentTerm getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerm paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	
	@Override
	protected void preCreate() {
		
	}

}
