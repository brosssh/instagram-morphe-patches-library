package app.morphe.library.instagram.patches

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/library/extension/instagram/patches/BlockUrlRequestPatch;"

private object MakeTigonRequestFingerprint : Fingerprint(
    definingClass = "Lcom/instagram/api/tigon/TigonServiceLayer;",
    name = "makeTigonRequest"
)

private val blockUrlBasePatch = bytecodePatch {
    dependsOn(instagramExtensionPatch)

    execute {
        with(MakeTigonRequestFingerprint.method) {
            val uriFieldIndex = indexOfFirstInstructionOrThrow {
                getReference<FieldReference>()?.type == "Ljava/net/URI;"
            }

            val uriField = getInstruction<TwoRegisterInstruction>(uriFieldIndex)
                .getReference<FieldReference>()!!

            val uriFieldDescriptor = "${uriField.definingClass}->${uriField.name}:Ljava/net/URI;"

            addInstructions(
                0,
                """
                    iget-object v0, p1, $uriFieldDescriptor
                    invoke-virtual {v0}, Ljava/net/URI;->toString()Ljava/lang/String;
                    move-result-object v0
                    invoke-static {v0}, $EXTENSION_CLASS_DESCRIPTOR->checkAndBlockUrl(Ljava/lang/String;)V
                """
            )
        }
    }
}

@Suppress("unused")
fun blockUrl(urlSubstring: String) = bytecodePatch {
    dependsOn(blockUrlBasePatch)

    execute {
        MakeTigonRequestFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "$urlSubstring"
                invoke-static {v0}, $EXTENSION_CLASS_DESCRIPTOR->addBlockedUrl(Ljava/lang/String;)V
            """
        )
    }
}
