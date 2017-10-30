package kr.co.bootpay.model

class Trace {
    private var page_type: String? = null

    private var category1: String? = null

    private var category2: String? = null

    private var category3: String? = null

    private var item_img: String? = null

    private var item_name: String? = null

    private var item_unique: String? = null

    fun setPageType(page_type: String?): Trace {
        this.page_type = page_type
        return this
    }

    fun setMainCategory(main: String?): Trace {
        category1 = main
        return this
    }

    fun setMiddleCategory(middle: String?): Trace {
        category2 = category2
        return this
    }

    fun setSubCategory(sub: String?): Trace {
        category3 = sub
        return this
    }

    fun setItemImage(image: String?): Trace {
        item_img = image
        return this
    }

    fun setItemName(name: String?): Trace {
        item_name = name
        return this
    }

    fun setItemUnique(unique: String?): Trace {
        item_unique = unique
        return this
    }
}
