<template>
  <body id="poster">
  <section class="login-container">
    <el-form class="" label-position="left"
             label-width="0px">
      <h3 class="login_title">教学成果打分系统登录</h3>
      <el-form-item :rules="rules">
        <el-input type="text" v-model="loginForm.username"
                  auto-complete="off" placeholder="账号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input type="text" v-model="loginForm.password"
                  auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" native-type="submit" style="width: 100%;background: #505458;border: none"  v-on:click="login">登录</el-button>
      </el-form-item>
    </el-form>
    <p class="tip">温馨提示：</p>
    <p class="tip">未登录过的新用户，自动注册</p>
    <p class="tip">登录过的用户可凭账号密码登录</p>
  </section>
  </body>
</template>
<style>
  #poster {
    /*background: url("../assets/Code.jpg") no-repeat;*/
    background-color: #324057;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  body {
    margin: 0px;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .tip{
    font-size: 12px;
    color: red;
  }

</style>
<script>
export default {
  name: 'Login',
  data () {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{required: true, message: '请输入用户名'}],
        password: [{required: true, message: '请输入密码'}]
      },
      responseResult: []
    }
  },
  methods: {
    async login () {
      if (!this.loginForm.username) {
        alert('请输入用户名')
        return false
      }
      if (!this.loginForm.password) {
        alert('请输入密码')
        return false
      }
      this.$axios
        .post('/login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        })
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            sessionStorage.setItem('username', JSON.parse(successResponse.data.msg).username)
            sessionStorage.setItem('userid', JSON.parse(successResponse.data.msg).id)
            console.log('成功拿到username' + JSON.parse(successResponse.data.msg).username)
            this.$router.push('/manage')
          } else {
            alert(successResponse.data.msg)
          }
        })
        .catch(failResponse => {
          alert('登录失败')
        })
    }
  }
}
</script>
