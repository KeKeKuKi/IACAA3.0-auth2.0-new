<template>
  <div style="background:-webkit-linear-gradient(#ffffff,#ffffff,#dcdcdc,#ffffff);">
    <el-carousel :interval="2000" type="card" height="500px" style="padding: 40px;margin-top: 5%;margin-right: 2%;">
      <el-carousel-item v-for="item in 6" :key="item">
        <div class="medium1">welcome ...</div>
        <div class="medium">IACAA - 2.0</div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>

export default {
  name: "Main"
}
</script>

<style scoped>
.medium1{
  font-size: 30px;
  color: white;
  font-family: Abduction2000;
  padding-top: 35%;
  margin-left: 5%;
}
.medium{
  font-size: 80px;
  color: white;
  font-family: Abduction2000;
  margin-top: 5%;
  margin-left: 35%;
}
.el-carousel__item:nth-child(2n) {
  background:-webkit-linear-gradient(#5200cd, #8901d4);
}

.el-carousel__item:nth-child(2n+1) {
  background:-webkit-linear-gradient(#0063d9, #01bd3e);
}
</style>
