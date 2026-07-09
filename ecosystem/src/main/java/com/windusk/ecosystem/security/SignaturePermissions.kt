package com.windusk.ecosystem.security

/**
 * Разрешения, действующие для приложений, подписанных одним разработчикам.
 */
object SignaturePermissions {
    /**
     *  Если предоставлено, компонент может быть подключен к компоненту.
     */
    const val CONNECT_TO_ECOSYSTEM = "com.windusk.ecosystem.CENTAUR_ECOSYSTEM_ACCESS"

    /**
     *  Если предоставлено, клиент будет подключаться к компоненту без предупреждения.
     */
    const val CONNECT_AS_SIGNED_COMPONENT_PERMISSION = "com.windusk.ecosystem.CONNECT_AS_SIGNED_COMPONENT"
}