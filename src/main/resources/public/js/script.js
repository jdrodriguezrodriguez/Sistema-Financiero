window.onload = function() {
    console.log("Página cargada con éxito.");
};


const nav = document.querySelector('.nav');

    window.addEventListener('scroll', function(){
        nav.classList.toggle('active', window.scrollY >0)
    })