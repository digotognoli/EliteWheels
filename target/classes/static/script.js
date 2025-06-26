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

// NOVA FUNÇÃO PARA CALCULAR VALOR FINAL
function calcularValorFinal() {
    const valorBaseEl = document.getElementById('valorBase');
    const descontoEl = document.getElementById('desconto');
    const valorFinalDisplayEl = document.getElementById('valorFinalDisplay');
    const valorFinalInputEl = document.getElementById('valorFinalInput');

    // Pega o valor base (removendo a formatação de moeda)
    const valorBase = parseFloat(valorBaseEl.innerText.replace('.', '').replace(',', '.'));
    
    // Pega o valor do desconto (garante que seja um número)
    const desconto = parseFloat(descontoEl.value) || 0;

    if (!isNaN(valorBase)) {
        const valorFinal = valorBase - desconto;
        
        // Formata para exibição (ex: 150.000,00)
        valorFinalDisplayEl.innerText = valorFinal.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        
        // Atualiza o valor no campo de formulário oculto
        valorFinalInputEl.value = valorFinal;
    }
}

// Chama a função uma vez no carregamento da página para inicializar o valor
window.onload = function() {
    if (document.getElementById('valorBase')) {
        calcularValorFinal();
    }
};