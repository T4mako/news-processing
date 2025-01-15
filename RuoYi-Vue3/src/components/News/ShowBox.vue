<template>
  <div class="main">
    <slot></slot>
  </div>
</template>

<script setup lang="ts" name="ShowBox">

</script>

<style scoped lang="scss">
.main {
  //background-color: #F5F5F5;
  border-radius: 4px;
  width: 100%;
  padding: 15px;

  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  margin-bottom: 10px;


}
</style>