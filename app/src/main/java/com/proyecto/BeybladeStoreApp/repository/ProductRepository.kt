package com.proyecto.BeybladeStoreApp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyecto.BeybladeStoreApp.data.models.Product
import kotlinx.coroutines.flow.map
import com.proyecto.BeybladeStoreApp.util.AppConfig
import com.proyecto.BeybladeStoreApp.util.DataStoreManager

class ProductRepository(private val context: Context) {
    private val gson = Gson()
    private val ds = DataStoreManager(context)


    suspend fun getProducts(): List<Product> {
        if (!AppConfig.USE_DATASTORE) {
            return getSampleProducts()
        }
        val json = ds.getProductCatalogJson()
        if (!json.isNullOrBlank()) {
            return try {
                val type = TypeToken.getParameterized(List::class.java, Product::class.java).type
                gson.fromJson<List<Product>>(json, type) ?: getSampleProducts().also { saveProducts(it) }
            } catch (e: Exception) {
                getSampleProducts().also { saveProducts(it) }
            }
        }
        val sample = getSampleProducts()
        saveProducts(sample)
        return sample
    }

    suspend fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        ds.saveProductCatalogJson(json)
    }

    fun productsFlow() = ds.productCatalogJsonFlow().map { json ->
        try {
            val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, Product::class.java).type
            val list: List<Product>? = gson.fromJson(json ?: "[]", type)
            list ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getSampleProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "Dragoon V",
                description = "Performance-type Beyblade",
                price = 29.99,
                imageResName = "gu967808_6_1",
                stock = 10
            ),
            Product(
                id = 2,
                name = "Rock Leone",
                description = "Defense-type Beyblade",
                price = 24.5,
                imageResName = "trompo_beyblade_x_pack_doble_beat_tyranno_4_70q_y_knife_shinobi_4_80hn",
                stock = 8
            ),
            Product(
                id = 3,
                name = "Storm Pegasus",
                description = "Balance-type Beyblade",
                price = 34.0,
                imageResName = "trompo_beyblade_x_kit_inicial_sword_dran_3_60f",
                stock = 12
            ),
            Product(
                id = 4,
                name = "Flame Sagittario",
                description = "Attack-type Beyblade",
                price = 31.75,
                imageResName = "beyblade_x_lanzador_premium_con_peonza_0_20250329060652",
                stock = 5
            ),
            Product(
                id = 5,
                name = "Aero Valkyrie",
                description = "Stamina-type Beyblade",
                price = 27.5,
                imageResName = "img_81v1up7cp1l",
                stock = 7
            ),
            Product(
                id = 6,
                name = "Phantom Orion",
                description = "Attack/Balance hybrid",
                price = 36.0,
                imageResName = "img_81cfov0s0zl__ac_uf894_1000_ql80_grande",
                stock = 6
            ),
            Product(
                id = 7,
                name = "Crimson Hydra",
                description = "Heavy hitter",
                price = 39.99,
                imageResName = "f38c373a_dbe0_4218_acb9_e97c2f29c433__15523_1731275026_2048x",
                stock = 4
            ),
            Product(
                id = 8,
                name = "Starter Pack",
                description = "Beginner friendly set",
                price = 19.99,
                imageResName = "w_1500_h_1500_fit_pad",
                stock = 20
            )
        )
    }
}

