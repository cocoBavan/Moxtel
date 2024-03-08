object Dependencies {
    val coreKtx: String by lazy { "androidx.core:core-ktx:${Versions.CORE_KTX}" }
    val coreSplash: String by lazy { "androidx.core:core-splashscreen:${Versions.SPLASH}" }

    val lifeCycleRuntime: String by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFE_CYCLE}" }


    val composeActivity: String by lazy { "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}" }
    val composeBom: String by lazy { "androidx.compose:compose-bom:${Versions.COMPOSE_BOM}" }
    val composeUI: String by lazy { "androidx.compose.ui:ui" }
    val composeUIGraphics: String by lazy { "androidx.compose.ui:ui-graphics" }
    val composeUITooling: String by lazy { "androidx.compose.ui:ui-tooling" }
    val composeUIToolingPreview: String by lazy { "androidx.compose.ui:ui-tooling-preview" }
    val composeMaterial: String by lazy { "androidx.compose.material3:material3" }
    val composeNavigation: String by lazy { "androidx.navigation:navigation-compose:${Versions.COMPOSE_NAVIGATION}" }
    val composeLifeCycleRuntime: String by lazy { "androidx.lifecycle:lifecycle-runtime-compose:${Versions.COMPOSE_LIFECYCLE}" }

    val asyncImage: String by lazy { "io.coil-kt:coil:${Versions.COIL}" }
    val asyncImageComposeExtension: String by lazy { "io.coil-kt:coil-compose:${Versions.COIL}" }

    val hilt: String by lazy { "com.google.dagger:hilt-android:${Versions.HILT}" }
    val hiltCompiler: String by lazy { "com.google.dagger:hilt-android-compiler:${Versions.HILT}" }
    val hiltNavigation: String by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.HILT_NAVIGATION}" }

    val coroutines: String by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}" }

    val retrofit: String by lazy { "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}" }
    val retrofitGson: String by lazy { "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}" }
    val httpLoggingInterceptor: String by lazy { "com.squareup.okhttp3:logging-interceptor:${Versions.HTTP_LOGGING_INTERCEPTOR}" }

    val unitTest: String by lazy { "junit:junit:${Versions.JUNIT}" }
    val unitTestMockk: String by lazy { "io.mockk:mockk:${Versions.MOCKK}" }
    val unitTestMockito: String by lazy { "org.mockito:mockito-core:${Versions.MOCKITO}" }

    val uiTest: String by lazy { "androidx.test.ext:junit:${Versions.UITEST}" }
    val uiTestExpresso: String by lazy { "androidx.test.espresso:espresso-core:${Versions.UITEST_EXPRESSO}" }
    val uiTestCompose: String by lazy { "androidx.compose.ui:ui-test-junit4" }
    val uiTestManifest: String by lazy { "androidx.compose.ui:ui-test-manifest" }
}
