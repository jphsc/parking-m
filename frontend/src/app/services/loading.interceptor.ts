import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { finalize, Observable } from 'rxjs';
import { LoadingService } from './loading.service';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  private totalRequests = 0;

  constructor(private service: LoadingService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    this.totalRequests++;

    if (this.totalRequests === 1) {
      this.service.show();
    }

    return next
      .handle(request)
      .pipe(
        finalize(() => {
          this.totalRequests--;

          if (this.totalRequests === 0) {
            this.service.hide();
          }
        })
      );
  }
}
