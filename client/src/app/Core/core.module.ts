import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SvgIconComponent } from "src/app/Shared/Components/SVGIcon/svg-icon.component";
import { HeaderComponent } from "./Components/Header/header.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [HeaderComponent, SvgIconComponent],
  imports: [CommonModule, RouterModule],
  exports: [RouterModule, SvgIconComponent, HeaderComponent]
})
export class CoreModule {}
