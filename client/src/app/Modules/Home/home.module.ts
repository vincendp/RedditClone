import { NgModule } from "@angular/core";
import { HomeComponent } from "./home.component";
import { SharedModule } from "src/app/Shared/shared.module";
import { HomeRoutingModule } from "./home-routing.module";
import { CoreModule } from "src/app/Core/core.module";

@NgModule({
  declarations: [HomeComponent],
  imports: [SharedModule, HomeRoutingModule, CoreModule]
})
export class HomeModule { }
