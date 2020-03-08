import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SvgIconComponent } from "src/app/Shared/Components/SVGIcon/svg-icon.component";
import { HeaderComponent } from "./Components/Header/header.component";
import { RouterModule } from "@angular/router";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { HttpHeaderInterceptor } from "./Interceptor/Http/http-header.interceptor";

@NgModule({
  declarations: [HeaderComponent, SvgIconComponent],
  imports: [CommonModule, RouterModule, HttpClientModule],
  exports: [RouterModule, SvgIconComponent, HeaderComponent],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpHeaderInterceptor,
      multi: true
    }
  ]
})
export class CoreModule {}
