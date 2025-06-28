package com.everbravo.gestordetareas.utils.enums

enum class ValidationResult(val description: String) {
    SUCCESS("SUCCESS"),
    LABEL_EMPTY("El campo no puede estar vacío"),
    LABEL_TOO_SHORT("El campo debe tener al menos 3 caracteres"),
    LABEL_INVALID("El campo de texto contiene caracteres no permitidos"),
    EMAIL_EMPTY("El correo electrónico no puede estar vacío"),
    EMAIL_INVALID("Ingresa un correo electrónico válido"),
    PASSWORD_EMPTY("La contraseña no puede estar vacía"),
    PASSWORD_TOO_SHORT("La contraseña debe tener al menos 6 caracteres"),
    USER_SUCCESS("Usuario registrado exitosamente"),
    USER_ERROR("Credenciales incorrectas"),
    TASK_ERROR("Error al almacenar la tarea"),
    TASK_SUCCESS("¡Tarea almacenada!"),
    LOCALIZATION_ERROR("Permiso de localización requerido"),
    SESSION_ERROR("Sesión finalizada"),
    USERNAME_ERROR("Nombre de usuario no disponible"),
    USERNAME_WELCOME("¡Bienvenido ")
}