import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { HttpHeaderInterceptor } from "./Interceptor/Http/http-header.interceptor";

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule, HttpClientModule],
  exports: [RouterModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpHeaderInterceptor,
      multi: true,
    },
  ],
})
export class CoreModule {}
