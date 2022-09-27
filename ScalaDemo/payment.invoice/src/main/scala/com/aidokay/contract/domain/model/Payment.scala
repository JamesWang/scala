package com.aidokay.contract.domain.model


case class Payment(
                    title: String,
                    date: String,
                    vendorNum: String,
                    vendor: String,
                    contractor: String,
                    weekBilled: String,
                    weekWorked: (String, String),
                    hours: Double,
                    rate: BigDecimal,
                    amount: BigDecimal,
                    tax: BigDecimal,
                    currency: String,
                    total: BigDecimal
                  ) extends Convertible