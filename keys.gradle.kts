import java.io.FileInputStream
import java.util.Properties

private fun getStringFromFile(fileName: String, property: String): String {
    val props = Properties()
    props.load(FileInputStream(rootProject.file(fileName)))
    return props.getProperty(property)
}

private fun getStringFromKeysFile(property: String): String {
    return getStringFromFile("keys.properties", property)
}

private val keys = mapOf(
    "apiHost" to getStringFromKeysFile("api.host"),
)

rootProject.extensions.extraProperties.set("apiSecrets", keys)