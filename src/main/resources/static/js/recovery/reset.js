import {recoveryEventos} from "/js/recovery/recoveryEventos.js";

document.addEventListener("DOMContentLoaded", () => {
    recoveryEventos();
});

/*OVERLAY - PASS*/
const overlayBlockPass = document.getElementById("forgot-password");
const overlayNonePass = document.getElementById("overlay-password");
const menuPasswordPass = document.getElementById("menu-password");


if (overlayBlockPass && overlayNonePass && menuPasswordPass) {
  overlayBlockPass.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNonePass.style.display = "block";
    menuPasswordPass.style.display = "block";
    overlayNoneUser.style.display = "none";
    menuPasswordUser.style.display = "none";
  });

  overlayNonePass.addEventListener("click", function () {
    overlayNonePass.style.display = "none";
    menuPasswordPass.style.display = "none";
    overlayNoneUser.style.display = "block";
    menuPasswordUser.style.display = "block";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}

/*OVERLAY - USER*/
const overlayBlockUser = document.getElementById("forgot-username");
const overlayNoneUser = document.getElementById("overlay-username");
const menuPasswordUser = document.getElementById("menu-username");

if (overlayBlockUser && overlayNoneUser && menuPasswordUser) {
  overlayBlockUser.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNoneUser.style.display = "block";
    menuPasswordUser.style.display = "block";
    overlayNonePass.style.display = "none";
    menuPasswordPass.style.display = "none";
  });

  overlayNoneUser.addEventListener("click", function () {
    overlayNoneUser.style.display = "none";
    menuPasswordUser.style.display = "none";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}

