package com.adg.stocksample.di

import android.content.Context
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FlipperModule {

    @Multibinds
    abstract fun provideFlipperPlugins(): Set<FlipperPlugin>

    @Binds
    @IntoSet
    @Singleton
    abstract fun provideNetworkFlipperPluginIntoSet(plugin: NetworkFlipperPlugin): FlipperPlugin

    companion object {

        @IntoSet
        @Provides
        fun provideViewInspectorPlugin(@ApplicationContext context: Context): FlipperPlugin {
            return InspectorFlipperPlugin(context, DescriptorMapping.withDefaults())
        }

        @Provides
        @Singleton
        fun provideOkHttpInspectorPlugin(): NetworkFlipperPlugin {
            return NetworkFlipperPlugin()
        }
    }
}