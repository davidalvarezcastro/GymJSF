/**
 * @author david {dac1005@alu.ubu.es}
 */

/**
 * Función para gestionar el modal de los ficheros
 */
function clickModalFiles() {
	// Get the modal
	var modal = document.getElementById("modal-ficheros");

	// Get the button that opens the modal
	var btn = document.getElementById("modal-ficheros-button");

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal
	btn.onclick = function() {
	  modal.style.display = "block";
	}

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
	  modal.style.display = "none";
	}
	
	if (event.target == modal) {
	  modal.style.display = "none";
	}
}

/**
 * Función para duplicar un grupo de DOM elements (inputs ficheros)
 *
 * @returns
 */
function anhadir_fichero () {
	let ficheros_dom = document.getElementById('new_files')
	let domElement = document.getElementsByClassName('add-file')[0]
	
	let clon = domElement.cloneNode("add-file")
	let inputs = clon.getElementsByTagName('input')
	for (var i=0, max=inputs.length; i < max; i++) {
		inputs[i].value = "";
	}

	ficheros_dom.appendChild(clon);
}

/**
 * Función que permite simular el click en el commandButton de logout
 *
 * @returns
 */
function clickLogout() {
	console.log("asdasdS")
    document.getElementById('form-logout:hide-button-logout').click();
}


/**
 * Función que permite modificar el título del menú según la vista
 *
 * @param titulo
 * @returns
 */
function setTitleBreadcrumb(titulo) {
	document.getElementById('breadcrumb-top-menu').innerHTML = titulo;
}
