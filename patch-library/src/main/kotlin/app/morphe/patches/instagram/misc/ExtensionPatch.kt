package app.morphe.patches.instagram.misc

import app.morphe.patcher.Fingerprint
import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch

val instagramExtensionPatch = sharedExtensionPatch(
    "instagram",
    ExtensionHook(
        Fingerprint(
            name = "onCreate",
            definingClass = "/InstagramAppShell;"
        )
    )
)
