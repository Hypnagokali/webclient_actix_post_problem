use actix_multipart::Multipart;
use actix_web::{App, HttpResponse, HttpServer, post, Responder};
use actix_web::web::{Bytes, Path};
use futures_util::StreamExt;

#[post("/upload/{id}")]
async fn post_upload(mut payload: Multipart, id: Path<u32>) -> impl Responder {
    println!("Request upload: id = {}", id);
    while let Some(item) = payload.next().await {
        let mut field = item.unwrap();
        
        let content_disposition = field.content_disposition().unwrap();
        let _ = content_disposition
            .get_filename()
            .map(|f| f.to_string())
            .unwrap_or_else(|| "file.bin".to_string());

        while let Some(chunk) = field.next().await {
            println!("Next chunk: {:?}", chunk.unwrap());
        }
    }
    HttpResponse::Ok().finish()
}

#[post("/do-some-action/{id}")]
async fn post_test(id: Path<u32>) -> impl Responder {
    println!("Request do-some-action: id = {}", id);
    HttpResponse::Ok().finish()
}

#[post("/do-some-action-read-body")]
async fn post_test_read_body(body: Bytes) -> impl Responder {
    println!("Request do-some-action-read-body");
    println!("Body length = {}", body.len()); // this is not needed.
    HttpResponse::Ok().finish()
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .service(post_test)
            .service(post_upload)
            .service(post_test_read_body)
    })
        // .keep_alive(Duration::from_secs(75))
        .bind(("127.0.0.1", 7070))?
        .run()
        .await
}

