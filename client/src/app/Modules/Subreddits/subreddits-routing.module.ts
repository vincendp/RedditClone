import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SubredditsComponent } from "./subreddits.component";
import { CreateSubredditsComponent } from "./create-subreddits.component";
import { ViewSubredditsComponent } from "./view-subreddits.component";

const routes: Routes = [
  {
    path: "",
    component: SubredditsComponent,
    children: [
      {
        path: "view",
        component: ViewSubredditsComponent,
      },
      {
        path: "create",
        component: CreateSubredditsComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SubredditsRoutingModule {}
