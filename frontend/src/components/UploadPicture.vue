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
  
          // Crear una vista previa de la imagen
          const reader = new FileReader();
          reader.onload = (e) => {
            this.preview = e.target.result;
          };
          reader.readAsDataURL(file);
        } else {
          alert("Por favor, selecciona un archivo de imagen válido.");
        }
      },
      uploadPhoto() {
        if (!this.selectedFile) return;
  
        const formData = new FormData();
        formData.append("profilePicture", this.selectedFile);
  
        // Emitir los datos al componente padre
        this.$emit("upload-photo", formData);
  
        // Limpiar los datos después de la subida
        this.selectedFile = null;
        this.preview = null;
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
  