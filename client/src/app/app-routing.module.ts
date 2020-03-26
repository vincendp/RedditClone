import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

const routes: Routes = [
  {
    path: "",
    loadChildren: () =>
      import("./Modules/Home/home.module").then(m => m.HomeModule)
  },
  {
    path: "Login",
    loadChildren: () =>
      import("./Modules/Login/login.module").then(m => m.LoginModule)
  },
  {
    path: "Signup",
    loadChildren: () =>
      import("./Modules/Signup/signup.module").then(m => m.SignupModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
