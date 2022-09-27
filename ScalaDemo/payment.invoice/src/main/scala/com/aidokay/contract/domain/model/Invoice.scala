package com.aidokay.contract.domain.model


case class Invoice(
                    title: String,
                    address_1: String,
                    address_2: String,
                    contractor: String,
                    phone: String,
                    invoice_num: String,
                    email: String,
                    assignment: String,
                    subtotal: BigDecimal,
                    hst: BigDecimal,
                    gst_number: String,
                    total: BigDecimal,
                    time_period: (String,String)
                  ) extends Convertible