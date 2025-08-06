window.onload = function() {
    console.log("Página cargada con éxito.");
};

/*NAV*/
const nav = document.querySelector('.nav');

    window.addEventListener('scroll', function(){
        nav.classList.toggle('active', window.scrollY >0)
    })


/*OVERLAY - UPDATE*/
document.getElementById("link-trigger-update").addEventListener("click", function(event) {
    event.preventDefault(); 
    document.getElementById("overlay-update").style.display = "block"; 
    document.getElementById("menu-update").style.display = "block"; 
});



document.getElementById("overlay-update").addEventListener("click", function() {
    document.getElementById("overlay-update").style.display = "none"; 
    document.getElementById("menu-update").style.display = "none"; 
});

//MOSTRAR ERROR UPDATE
/*
<script>
    const params = new URLSearchParams(window.location.search);
    if (params.get("error") === "username") {
        document.getElementById("error-message").textContent = "El nombre de usuario ya está en uso.";
    }
</script>

<p id="error-message" style="color:red;"></p>

*/ 