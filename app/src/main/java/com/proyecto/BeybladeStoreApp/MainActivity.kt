package com.proyecto.BeybladeStoreApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.produceState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.BeybladeStoreApp.ui.theme.screen.RegisterRoute
import com.proyecto.BeybladeStoreApp.ui.theme.screen.SharedTopBar
import com.proyecto.BeybladeStoreApp.ui.theme.screen.ProductDetailScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.SplashRoute
import com.proyecto.BeybladeStoreApp.ui.theme.screen.CartScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.SettingsRoute
import com.proyecto.BeybladeStoreApp.ui.theme.screen.ProfileScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.OrdersScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminAddProductScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminHubScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminOrdersScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminUsersScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminEditUserScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminEditProductScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.ProductListScreen
import com.proyecto.BeybladeStoreApp.ui.theme.screen.AdminCatalogScreen
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.BeybladeStoreApp.ui.theme.BeybladeStoreAppTheme
import com.proyecto.BeybladeStoreApp.ui.theme.screen.LoginRoute
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.MoreVert

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            BeybladeStoreAppTheme(darkTheme = true) {
                androidx.compose.runtime.remember {
                    Unit
                }
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val ctx = LocalContext.current


                val logoutAndNavigate: () -> Unit = {
                    scope.launch {
                        try {
                            com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).setSession(null)
                        } catch (_: Throwable) { }
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashRoute(onContinue = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }
                    composable("login") {
                        LoginRoute(
                            onNavigateToRegister = { navController.navigate("register") },
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("register") {
                        RegisterRoute(onNavigateToLogin = { navController.popBackStack() })
                    }
                    composable("home") {
                        HomeScreen(onLogout = logoutAndNavigate, onSettings = { navController.navigate("settings") }, onProductClick = { id ->
                            navController.navigate("detail/$id")
                        }, onCartClick = { navController.navigate("cart") }, onOrdersClick = { navController.navigate("orders") }, onAdminClick = { navController.navigate("admin") }, onEditProduct = { id -> navController.navigate("admin/editproduct/$id") })
                    }

                    composable("detail/{productId}") { backStackEntry ->
                        val pid = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                        val ctx = LocalContext.current
                        val scope = rememberCoroutineScope()
                        val productState by produceState(initialValue = null as com.proyecto.BeybladeStoreApp.data.models.Product?, key1 = pid, producer = {
                            value = try {
                                if (pid == null) null else com.proyecto.BeybladeStoreApp.repository.ProductRepository(ctx).getProducts().firstOrNull { it.id == pid }
                            } catch (t: Throwable) {
                                null
                            }
                        })
                        productState?.let { p ->

                            val cartRepoLocal = com.proyecto.BeybladeStoreApp.repository.CartRepository(ctx)
                            val cartFactoryLocal = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel(cartRepoLocal) as T
                                }
                            }
                                val cartVmLocal: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel = viewModel(factory = cartFactoryLocal)


                                val currentUserState by produceState(initialValue = null as String?, key1 = ctx) {
                                    value = try {
                                        com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).getSession()
                                    } catch (t: Throwable) {
                                        null
                                    }
                                }
                                val currentUser = currentUserState

                                val cartItemsState by cartVmLocal.items.collectAsState()
                                val cartCount = cartItemsState.size

                                ProductDetailScreen(
                                    product = p,
                                    currentUser = currentUser,
                                    cartCount = cartCount,
                                    onAddToCart = { id, qty -> cartVmLocal.addToCart(id, qty) },
                                    onCartClick = { navController.navigate("cart") },
                                    onOrdersClick = { navController.navigate("orders") },
                                    onAdminClick = { navController.navigate("admin") },
                                    onSettings = { navController.navigate("settings") },
                                    onLogout = logoutAndNavigate,
                                    onBack = { navController.popBackStack() }
                                )
                        }
                    }

                    composable("cart") {
                        CartScreen(
                            onBack = { navController.popBackStack() },
                            onCartClick = {  },
                            onOrdersClick = { navController.navigate("orders") },
                            onAdminClick = { navController.navigate("admin") },
                            onSettings = { navController.navigate("settings") },
                            onLogout = logoutAndNavigate
                        )
                    }

                    composable("admin") {
                        AdminHubScreen(
                            onAddProduct = { navController.navigate("admin/add") },
                            onManageOrders = { navController.navigate("admin/orders") },
                            onManageUsers = { navController.navigate("admin/users") },
                            onManageCatalog = { navController.navigate("admin/catalog") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("admin/add") {
                        AdminAddProductScreen(onSaved = { navController.popBackStack() }, onCancel = { navController.popBackStack() })
                    }

                    composable("admin/orders") {
                        AdminOrdersScreen(onBack = { navController.popBackStack() })
                    }

                    composable("admin/users") {
                        AdminUsersScreen(onBack = { navController.popBackStack() }, onEditUser = { email ->
                            val encoded = java.net.URLEncoder.encode(email, "UTF-8")
                            navController.navigate("admin/edituser/$encoded")
                        })
                    }

                    composable("admin/edituser/{email}") { backStackEntry ->
                        val e = backStackEntry.arguments?.getString("email")
                        val decoded = if (e != null) java.net.URLDecoder.decode(e, "UTF-8") else null
                        decoded?.let { email ->
                            AdminEditUserScreen(userEmail = email, onBack = { navController.popBackStack() })
                        }
                    }

                    composable("admin/catalog") {
                        AdminCatalogScreen(onBack = { navController.popBackStack() }, onEditProduct = { id -> navController.navigate("admin/editproduct/$id") })
                    }

                    composable("admin/editproduct/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        id?.let { pid ->
                            AdminEditProductScreen(productId = pid, onBack = { navController.popBackStack() })
                        }
                    }

                    composable("orders") {
                        OrdersScreen(onBack = { navController.popBackStack() })
                    }
                    composable("settings") {
                        SettingsRoute(onBack = { navController.popBackStack() }, onAdminAdd = { navController.navigate("admin") }, onViewOrders = { navController.navigate("orders") }, onProfile = { navController.navigate("profile") })
                    }
                    composable("profile") {
                        ProfileScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyPreviewHost() {

    val ctx = LocalContext.current
}

@Composable
fun SimpleHome(onLogout: () -> Unit) {
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text("Bienvenido a Beyblade Store", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
        androidx.compose.material3.Button(onClick = onLogout) {
            androidx.compose.material3.Text("Cerrar sesión")
        }
    }
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
fun HomeScreen(
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onProductClick: (Int) -> Unit = {},
    onCartClick: () -> Unit = {},
    onOrdersClick: () -> Unit = {},
    onAdminClick: () -> Unit = {},
    onEditProduct: (Int) -> Unit = {}
) {
    val ctx = LocalContext.current


    val sessionState = produceState(initialValue = null as String?, key1 = ctx) {
        try {
            val repo = com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx)
            value = repo.getSession()
        } catch (t: Throwable) {
            value = null
        }
    }
    val currentUser = sessionState.value


    val productRepo = remember { com.proyecto.BeybladeStoreApp.repository.ProductRepository(ctx) }
    val productFactory = remember(productRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.ProductsViewModel(productRepo) as T
            }
        }
    }
    val productsVm: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.ProductsViewModel = viewModel(factory = productFactory)
    val products by productsVm.products.collectAsState()


    val cartRepo = remember { com.proyecto.BeybladeStoreApp.repository.CartRepository(ctx) }
    val cartFactory = remember(cartRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel(cartRepo) as T
            }
        }
    }
    val cartVm: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel = viewModel(factory = cartFactory)
    val cartCount by cartVm.items.collectAsState()
    val cartCountSize = cartCount.size

    androidx.compose.material3.Scaffold(
        topBar = {
            SharedTopBar(currentUser = currentUser, cartCount = cartCountSize, onCartClick = onCartClick, onOrdersClick = onOrdersClick, onAdminClick = onAdminClick, onSettings = onSettings, onLogout = onLogout)
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(padding)) {
            androidx.compose.foundation.layout.Column {
                ProductListScreen(
                    products = products,
                    modifier = androidx.compose.ui.Modifier.weight(1f),
                    isAdmin = (currentUser == "admin"),
                    onProductClick = onProductClick,
                    onEditProduct = onEditProduct
                )

                androidx.compose.foundation.layout.Row(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    if (currentUser != "admin") {
                        androidx.compose.material3.Button(onClick = onCartClick) {
                            androidx.compose.material3.Text("Ver mi carrito")
                        }
                    }

                    androidx.compose.material3.Button(onClick = onSettings) {
                        androidx.compose.material3.Text("Ajustes")
                    }
                }
            }
        }
    }
}

