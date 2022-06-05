package com.kelp.imanapp

class data_barang {
    var kodebrg: String? = null
    var namabrg: String? = null
    var jmlbrg: String? = null
    var key: String? = null

    constructor() {}

    constructor(kodebrg: String?, namabrg: String?, jmlbrg: String?) {
        this.kodebrg = kodebrg
        this.namabrg = namabrg
        this.jmlbrg = jmlbrg
    }
}