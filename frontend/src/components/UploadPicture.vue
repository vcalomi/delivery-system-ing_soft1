<template>
  <div 
    class="drag-drop-container"
    @dragover.prevent
    @drop.prevent="onDrop"
  >
    <div 
      class="drag-drop-box" 
      :class="{ 'dragging': isDragging }" 
      @dragenter="isDragging = true" 
      @dragleave="isDragging = false"
    >
      <p v-if="!preview">Arrastra una imagen aquí o haz clic para seleccionarla</p>
      <img v-if="preview" :src="preview" alt="Vista previa" class="img-preview" />
    </div>
    <input 
      type="file" 
      accept="image/*" 
      ref="fileInput" 
      @change="onFileChange" 
      class="file-input"
    />
    <button 
      class="btn btn-primary mt-2" 
      :disabled="!selectedFile" 
      @click="uploadPhoto"
    >
      Subir Foto
    </button>
  </div>
</template>

  
<script>
import axios from "axios";

export default {
  data() {
    return {
      selectedFile: null,
      preview: null,
      isDragging: false,
    };
  },
  methods: {
    onFileChange(event) {
      const file = event.target.files[0];
      this.handleFile(file);
    },
    onDrop(event) {
      const file = event.dataTransfer.files[0];
      this.handleFile(file);
      this.isDragging = false;
    },
    handleFile(file) {
      if (file && file.type.startsWith("image/")) {
        this.selectedFile = file;

        // vista previa de la imagen
        const reader = new FileReader();
        reader.onload = (e) => {
          this.preview = e.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        alert("Por favor, selecciona un archivo de imagen válido.");
      }
    },
    async uploadPhoto() {
      if (!this.selectedFile) return;

    //imagen a Base64
      const reader = new FileReader();
      reader.onload = async (e) => {
        const base64Image = e.target.result.split(",")[1]; // elimina el prefijo de la cadena Base64

        try {
          await axios.post(
            "http://localhost:8081/api/users/profilePicture",
            { profilePicture: base64Image },
            { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
          );

          alert("Foto de perfil subida con éxito.");
          this.reset();
        } catch (err) {
          console.error("Error al subir la foto de perfil:", err);
          alert("Hubo un error al subir la foto.");
        }
      };
      reader.readAsDataURL(this.selectedFile);
    },
    reset() {
      this.selectedFile = null;
      this.preview = null;
      this.isDragging = false;
    },
  },
};
</script>

  
<style scoped>
.drag-drop-container {
  text-align: center;
}

.drag-drop-box {
  width: 300px;
  height: 200px;
  border: 2px dashed #ddd;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: border-color 0.3s ease;
}

.drag-drop-box.dragging {
  border-color: #007bff;
}

.img-preview {
  max-width: 100%;
  max-height: 100%;
  border-radius: 5px;
  object-fit: cover;
}

.file-input {
  display: none;
}
</style>
