import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SubredditComponent } from "./subreddit.component";

const routes: Routes = [
  {
    path: "",
    component: SubredditComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SubredditRoutingModule {}
