import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HeaderComponent } from "./Components/Header/header.component";
import { SvgIconComponent } from "src/app/Shared/Components/SVGIcon/svg-icon.component";

@NgModule({
  declarations: [HeaderComponent, SvgIconComponent],
  imports: [CommonModule],
  exports: [HeaderComponent]
})
export class CoreModule {}
