package kr.co.bootpay.model

class TraceKotlin {
    private var page_type: String? = null

    private var category1: String? = null

    private var category2: String? = null

    private var category3: String? = null

    private var item_img: String? = null

    private var item_name: String? = null

    private var item_unique: String? = null

    fun setPageType(page_type: String?): TraceKotlin {
        this.page_type = page_type
        return this
    }

    fun setMainCategory(main: String?): TraceKotlin {
        category1 = main
        return this
    }

    fun setMiddleCategory(middle: String?): TraceKotlin {
        category2 = category2
        return this
    }

    fun setSubCategory(sub: String?): TraceKotlin {
        category3 = sub
        return this
    }

    fun setItemImage(image: String?): TraceKotlin {
        item_img = image
        return this
    }

    fun setItemName(name: String?): TraceKotlin {
        item_name = name
        return this
    }

    fun setItemUnique(unique: String?): TraceKotlin {
        item_unique = unique
        return this
    }
}
