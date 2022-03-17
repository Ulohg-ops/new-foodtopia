from clarifai_grpc.channel.clarifai_channel import ClarifaiChannel
from clarifai_grpc.grpc.api import service_pb2_grpc, service_pb2, resources_pb2
from clarifai_grpc.grpc.api.status import status_code_pb2

stub = service_pb2_grpc.V2Stub(ClarifaiChannel.get_grpc_channel())
metadata = (('authorization', f'Key {"bb64fb1392674cd7822192500aa429c0"}'),)
request = service_pb2.PostModelOutputsRequest(
    # This is the model ID of a publicly available General model. You may use any other public or custom model ID.
    model_id='food-item-v1-recognition',
    inputs=[
        resources_pb2.Input(data=resources_pb2.Data(image=resources_pb2.Image(url='https://picsum.photos/id/1080/200/300')))
    ])
response = stub.PostModelOutputs(request, metadata=metadata)
if response.status.code != status_code_pb2.SUCCESS:
    print("There was an error with your request!")
    print("\tCode: {}".format(response.outputs[0].status.code))
    print("\tDescription: {}".format(response.outputs[0].status.description))
    print("\tDetails: {}".format(response.outputs[0].status.details))
    raise Exception("Request failed, status code: " + str(response.status.code))

def getInfo():
    for concept in response.outputs[0].data.concepts:
        print('%12s: %.2f' % (concept.name, concept.value))