document.addEventListener('DOMContentLoaded', () => {
    // Seleciona todos os links que devem acionar a transição
    const transitionLinks = document.querySelectorAll('.page-transition-link');

    transitionLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault(); // Impede a navegação imediata
            const destination = this.href;

            // Adiciona a classe para a animação de saída
            document.body.classList.add('fade-out');

            // Aguarda a animação terminar e então navega
            setTimeout(() => {
                window.location.href = destination;
            }, 500); // Duração da animação em milissegundos
        });
    });
});
