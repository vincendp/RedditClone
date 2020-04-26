import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

const routes: Routes = [
  {
    path: "",
    loadChildren: () =>
      import("./Modules/Home/home.module").then((m) => m.HomeModule),
  },
  {
    path: "login",
    loadChildren: () =>
      import("./Modules/Login/login.module").then((m) => m.LoginModule),
  },
  {
    path: "signup",
    loadChildren: () =>
      import("./Modules/Signup/signup.module").then((m) => m.SignupModule),
  },
  {
    path: "subreddits",
    loadChildren: () =>
      import("./Modules/Subreddits/subreddits.module").then((m) => m.SubredditsModule),
  },
  {
    path: "r/:subreddit",
    loadChildren: () =>
      import("./Modules/Subreddit/subreddit.module").then((m) => m.SubredditModule),
  },
  {
    path: "r/:subreddit/:post_id",
    loadChildren: () =>
      import("./Modules/Post/post.module").then((m) => m.PostModule),
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
