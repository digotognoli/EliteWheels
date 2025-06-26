
function validarFormulario() {
    const campos = document.querySelectorAll('input[required], select[required]');
    for (const campo of campos) {
        if (!campo.value.trim()) {
            alert("Por favor, preencha todos os campos obrigatórios.");
            return false;
        }
    }
    return true;
}
