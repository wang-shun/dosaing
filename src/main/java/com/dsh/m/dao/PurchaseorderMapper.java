package com.dsh.m.dao;

import com.dsh.m.model.Purchaseorder;
import com.dsh.m.model.PurchaseorderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PurchaseorderMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int countByExample(PurchaseorderExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int deleteByExample(PurchaseorderExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int insert(Purchaseorder record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int insertSelective(Purchaseorder record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	List<Purchaseorder> selectByExample(PurchaseorderExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	Purchaseorder selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int updateByExampleSelective(@Param("record") Purchaseorder record,
			@Param("example") PurchaseorderExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int updateByExample(@Param("record") Purchaseorder record,
			@Param("example") PurchaseorderExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int updateByPrimaryKeySelective(Purchaseorder record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table purchaseorder
	 * @mbggenerated  Wed Aug 19 09:56:02 CST 2015
	 */
	int updateByPrimaryKey(Purchaseorder record);
}