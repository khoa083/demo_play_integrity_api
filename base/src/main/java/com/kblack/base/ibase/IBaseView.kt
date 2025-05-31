package com.kblack.project_base.base.ibase

interface IBaseView {
    /**
     * Init interface to pass parameters
     */
    fun initParam()
    /**
     * Init data
     */
    fun initData()
    /**
     * Init the observer monitoring interface
     */
    fun initViewObservable()
}