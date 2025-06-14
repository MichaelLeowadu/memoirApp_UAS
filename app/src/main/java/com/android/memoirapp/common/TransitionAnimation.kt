package com.android.memoirapp.common

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(250, delayMillis = 120),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(250, delayMillis = 120))
}

sealed class ScaleTransitionDirection {
    object INWARDS: ScaleTransitionDirection()
    object OUTWARDS: ScaleTransitionDirection()
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = 120
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 120))
}