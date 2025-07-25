package com.github.binarywang.wxpay.bean.marketing.transfer;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <pre>
 * 微信支付批次单号查询批次单API
 * 文档地址：https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transfer_partner/chapter3_2.shtml
 * </pre>
 *
 * @author xiaoqiang
 * created on  2021-12-06
 */
@Data
@NoArgsConstructor
public class BatchNumberRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * <pre>
   * 字段名：微信支付批次单号
   * 变量名：batch_id
   * 是否必填：是
   * 类型：string[32, 64]
   * 描述：
   *  path微信支付批次单号，微信商家转账系统返回的唯一标识
   *  示例值：1030000071100999991182020050700019480001
   * </pre>
   */
  @SerializedName(value = "batch_id")
  private String batchId;

  /**
   * <pre>
   * 字段名：是否查询转账明细单
   * 变量名：need_query_detail
   * 是否必填：是
   * 类型：boolean 默认否
   * 描述：
   *  商户可选择是否查询指定状态的转账明细单，当转账批次单状态为“FINISHED”（已完成）时，才会返回满足条件的转账明细单
   *  示例值：true
   * </pre>
   */
  @SerializedName(value = "need_query_detail")
  private Boolean needQueryDetail;

  /**
   * <pre>
   * 字段名：请求资源起始位置
   * 变量名：offset
   * 是否必填：否
   * 类型：int
   * 描述：
   *  query该次请求资源的起始位置。返回的明细是按照设置的明细条数进行分页展示的，一次查询可能无法返回所有明细，我们使用该参数标识查询开始位置，默认值为0
   *  示例值：0
   * </pre>
   */
  @SerializedName(value = "offset")
  private Integer offset;

  /**
   * <pre>
   * 字段名：最大资源条数
   * 变量名：limit
   * 是否必填：否
   * 类型：int
   * 描述：
   *  query该次请求可返回的最大明细条数，最小20条，最大100条，不传则默认20条。不足20条按实际条数返回
   * 示例值：20
   * </pre>
   */
  @SerializedName(value = "limit")
  private Integer limit;

  /**
   * <pre>
   * 字段名：明细状态
   * 变量名：detail_status
   * 是否必填：否
   * 类型：string[1, 32]
   * 描述：
   *  query查询指定状态的转账明细单，不传没有明细状态信息返回。当need_query_detail为true时，该字段必填
   *  枚举值：
   *     ALL:全部。需要同时查询转账成功和转账失败的明细单
   *     SUCCESS:转账成功。只查询转账成功的明细单
   *     FAIL:转账失败。只查询转账失败的明细单
   *  示例值：FAIL
   * </pre>
   */
  @SerializedName(value = "detail_status")
  private String detailStatus;
}
