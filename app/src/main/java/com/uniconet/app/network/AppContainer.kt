package com.uniconet.app.network

import android.content.Context
import com.uniconet.app.data.AuthRepository
import com.uniconet.app.data.BookRepository
import com.uniconet.app.data.ForoRepository
import com.uniconet.app.data.GrupoRepository


class AppContainer(context: Context) {
    val authRepository = AuthRepository(context.applicationContext)
    val bookRepository = BookRepository(context.applicationContext)
    val foroRepository = ForoRepository(context.applicationContext)
    val grupoRepository = GrupoRepository(context.applicationContext)

    // Cliente Retrofit a AWS (IP elástica). Si el servidor no responde,
    // los repos locales siguen funcionando.
    val api: UniconetApi = NetworkModule.createApi()
}
